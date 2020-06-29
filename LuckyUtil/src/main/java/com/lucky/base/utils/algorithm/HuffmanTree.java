package com.lucky.base.utils.algorithm;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 哈夫曼树即最优二叉树
 * 给定n个权值作为n的[叶子]结点，构造一棵二叉树，若带权路径长度达到最小，称这样的二叉树为最优二叉树，也称为哈夫曼树(Huffman Tree)。哈夫曼树是带权路径长度最短的树，权值较大的结点离根较近。
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/6/29 11:14
 */
public class HuffmanTree {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuffmanTree.class);

    public byte[] compress(String rawData) throws Exception {
        if(null == rawData){
            return null;
        }

        long startTime = System.currentTimeMillis();

        /*
         * 1.构建最优二叉树
         */
        HuffmanTree huffmanTree = buildHuffmanTree(rawData);

        /*
         * 2.根据最优二叉树获取字符和编码的关系
         */
        Map<Character, String> characterStringMap = new ConcurrentHashMap<>(16);
        String encode = "";
        traverseTree(huffmanTree.root, encode, characterStringMap);

        /*
         * 43编码
         * 此处为方便解码,采取两种方式
         * 1.将哈夫曼树和编码以key-value的形式存储redis
         * 2.将构建哈夫曼树的字符权重放到编码前面以;隔开
         */
        StringBuffer buffer = new StringBuffer();
        for (char c : rawData.toCharArray()) {
            buffer.append(characterStringMap.get(c));
        }
        String compressed = buffer.toString();
        LOGGER.info("------------HuffmanEncodingCompress------:编码详情:{}",compressed);

        /**
         * 5.二进制字符串转byte[]
         * 8位=1字节,不足8位后面用0补齐8位
         */
        int offset = 0;
        StringBuffer sb = new StringBuffer(compressed);
        while (sb.length() % 8 != 0){
            sb.append(0);
            offset++;
        }
        int size = sb.length() / 8;
        byte[] data = new byte[size];
        for(int i = 0 ; i < size ; i++){
            int startIndex = i * 8;
            int endIndex = (i + 1) * 8;
            data[i] = Long.valueOf(sb.substring(startIndex,endIndex), 2).byteValue();
        }

        stringRedisTemplate.opsForValue().set(buffer.toString(), JSON.toJSONString(huffmanTree));

        long endTime = System.currentTimeMillis();

        /*
         * 计算压缩性能
         */
        DecimalFormat df = new DecimalFormat("##.00%");
        int encodeSize =  (int)Math.ceil((double)data.length);
        String compressionRatio = df.format((double)encodeSize / rawData.getBytes().length);
        log.info("------HuffmanEncodingCompress------:原数据大小(单位:字节):{},压缩之后数据大小(单位:字节):{},压缩比:{},压缩耗费时间(单位:毫秒):{}",
                rawData.getBytes().length,
                data.length,
                compressionRatio,
                endTime - startTime);


        return data;
    }

    public byte[] unZip(String compressedData) throws Exception {

        if(null == compressedData){
            return null;
        }

        long startTime = System.currentTimeMillis();

        HuffmanTree huffmanTree = JSONObject.parseObject(stringRedisTemplate.opsForValue().get(compressedData), HuffmanTree.class);

        LinkedList<Character> characterLinkedList = new LinkedList<Character>();
        for(char c : compressedData.toCharArray()){
            characterLinkedList.add(c);
        }
        StringBuffer buffer = new StringBuffer();
        while (characterLinkedList.size() > 0){
            Node node = huffmanTree.root;
            do{
                Character c = characterLinkedList.removeFirst();
                if(StringUtils.equals("0",c.toString())){
                    node = node.leftNode;
                }else{
                    node = node.rightNode;
                }
            }while (!node.isLeaf);
            buffer.append(node.character);
        }

        long endTime = System.currentTimeMillis();

        log.info("------HuffmanEncodingCompress------:解压耗费时间(单位:毫秒):{}", endTime - startTime);

        return buffer.toString().getBytes();
    }

    /**
     * @description 构建最优二叉树方法
     * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
     * @date 2020/6/29 13:57
     * @param s
     * @return com.lucky.base.utils.algorithm.HuffmanEncoding.HuffmanTree
     * @throws Exception
     */
    private HuffmanTree buildHuffmanTree(String s){
        HuffmanTree huffmanTree = new HuffmanTree();

        Map<Character,Integer> map = statisticalCharacter(s);

        // 优先级队列
        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            Node node = new Node();
            node.character = entry.getKey().toString();
            node.weights = entry.getValue();
            node.isLeaf = true;
            priorityQueue.add(node);
        }

        while (priorityQueue.size() != 1){
            Node leftNode = priorityQueue.poll();
            Node rightNode = priorityQueue.poll();
            if(Objects.isNull(leftNode) || Objects.isNull(rightNode)){
                LOGGER.warn("------左右叶子节点均为空,返回空树!");
                return huffmanTree;
            }

            Node parentNode = new Node();

            parentNode.weights = leftNode.weights + rightNode.weights;
            parentNode.character = leftNode.character + rightNode.character;
            parentNode.leftNode = leftNode;
            parentNode.rightNode = rightNode;
            parentNode.isLeaf = false;
            leftNode.parent = parentNode;
            rightNode.parent = parentNode;
            priorityQueue.add(parentNode);
        }

        huffmanTree.root = priorityQueue.poll();
        return huffmanTree;
    }

    /**
     * @description 递归遍历哈夫曼树
     * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
     * @date 2020/6/29 13:56
     * @param node
     * @param encode
     * @param map
     * @return void
     */
    private void traverseTree(Node node, String encode, Map<Character, String> map){
        if (node.isLeaf){
            map.put(node.character.charAt(0), encode);
            return;
        }
        traverseTree(node.leftNode, encode + "0" ,map);
        traverseTree(node.rightNode, encode + "1", map);
    }


    /**
     * @description 统计字符个数
     * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
     * @date 2020/6/29 13:56
     * @param s
     * @return java.util.Map<java.lang.Character,java.lang.Integer>
     */
    private Map<Character,Integer> statisticalCharacter(String s){
        Map<Character,Integer> map = new ConcurrentHashMap<>(16);

        if(StringUtils.isEmpty(s)){
            return map;
        }

        List<Character> characterList = new ArrayList<>();
        for(char c : s.toCharArray()){
            characterList.add(c);
        }

        Map<Character, IntSummaryStatistics> collect = characterList.stream().collect(
                Collectors.groupingBy(Character::charValue,
                        Collectors.summarizingInt(Character::charValue)));

        for (Map.Entry<Character, IntSummaryStatistics> entry : collect.entrySet()) {
            IntSummaryStatistics intSummaryStatistics = entry.getValue();
            map.put(entry.getKey(),(int)intSummaryStatistics.getCount());
        }

        return map;
    }

    /**
     * 静态内部类 哈夫曼树
     * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
     * @date 2020/6/29 13:58
     */
    static class HuffmanTree{

        private Node root;

        public void setRoot(Node root){
            this.root = root;
        }

        public Node getRoot(){
            return root;
        }
    }

    /**
     * 静态内部类 树节点
     * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
     * @date 2020/6/29 13:58
     */
    static class Node implements Comparable<Node>{

        /**
         * 字符
         */
        private String character = "";

        /**
         * 字符权重
         */
        private int weights = 0;

        /**
         * 父节点
         */
        private Node parent;

        /**
         * 左节点
         */
        private Node leftNode;

        /**
         * 右节点
         */
        private Node rightNode;

        /**
         * 是否是叶子节点
         */
        private Boolean isLeaf;

        @Override
        public int compareTo(Node node) {
            return weights - node.weights;
        }

        public  Boolean isRoot(){
            return Objects.isNull(parent);
        }

        public Boolean isLeftNode(){
            return Objects.nonNull(parent) && this == parent.leftNode;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public int getWeights() {
            return weights;
        }

        public void setWeights(int weights) {
            this.weights = weights;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(Node leftNode) {
            this.leftNode = leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }

        public void setRightNode(Node rightNode) {
            this.rightNode = rightNode;
        }

        public Boolean getLeaf() {
            return isLeaf;
        }

        public void setLeaf(Boolean leaf) {
            isLeaf = leaf;
        }
    }
}
