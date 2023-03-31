# 基于Vue 3 + TypeScript + Vite构建

## 路由框架
## 全局状态管理框架
## 前端http框架
## JavaScript代码检查工具

### 插件安装以及初始化
```shell
pnpm add eslint -D

pnpm eslint --init
```

### 插件配置
* `.eslintrc.js`文件需要在env字段中添加`node:true`解决eslint找不到module的报错
* `package.json`的script中添加lint命令 `"lint": "eslint . --ext .vue,.js,.ts,.jsx,.tsx --fix"`
* eslint默认不会解析.vue文件后缀，需要额外配置解析器来解析.vue后缀文件
```json
   module.exports = {
      "env": {
         "browser": true,
         "es2021": true,
         "node": true
      },
      "extends": [
         "eslint:recommended",
         "plugin:vue/vue3-essential",
         "plugin:@typescript-eslint/recommended"
      ],
      "parser": "vue-eslint-parser",
      "parserOptions": {
         "ecmaVersion": "latest",
         "parser": "@typescript-eslint/parser",
         "sourceType": "module"
      },
      "plugins": [
         "vue",
         "@typescript-eslint"
      ],
      "rules": {
      }
   }
```

### 插件使用
```shell
pnpm lint
```
### vscode安装eslint插件
* `.vscode`目录下新增`setting.json`文件，添加配置
```json
{
    // 开启自动修复
    "editor.codeActionsOnSave": {
        "source.fixAll": false,
        "source.fixAll.eslint": true
    }
}
```
## 代码格式化工具
### 插件安装
```shell
pnpm add prettier -D
```
### 插件配置
* 根目录下新建`prettierrc.js`，添加配置
```json
module.exports = {
    // 一行的字符数，如果超过会进行换行，默认为80
    printWidth: 80, 
    // 一个tab代表几个空格数，默认为80
    tabWidth: 2, 
    // 是否使用tab进行缩进，默认为false，表示用空格进行缩减
    useTabs: false, 
    // 字符串是否使用单引号，默认为false，使用双引号
    singleQuote: true, 
    // 行位是否使用分号，默认为true
    semi: false, 
    // 是否使用尾逗号，有三个可选值"<none|es5|all>"
    trailingComma: "none", 
    // 对象大括号直接是否有空格，默认为true，效果：{ foo: bar }
    bracketSpacing: true
}
```
* `package.json`的script中添加format命令 `"format": "prettier --write \"./**/*.{html,vue,ts,js,json,md}\"",`
### vscode安装 Prettier Formatter插件
* `.vscode/settings.json`文件添加规则:
```json
{
    // 保存的时候自动格式化
    "editor.formatOnSave": true,
    // 默认格式化工具选择prettier
    "editor.defaultFormatter": "esbenp.prettier-vscode"
}
```
### 解决eslint和prettier冲突
```shell
pnpm add eslint-config-prettier eslint-plugin-prettier -D
```
在`.eslintrc.cjs`中extends的最后添加一个配置`'plugin:prettier/recommended'`
## CSS样式检查工具
### 插件安装
```shell
pnpm add stylelint postcss postcss-less postcss-html stylelint-config-prettier stylelint-config-recommended-less stylelint-config-standard stylelint-config-standard-vue stylelint-less stylelint-order -D
```
### 插件配置
* 新建`.stylelintrc.js`文件，添加配置
```javascript
module.exports = {
  extends: [
    'stylelint-config-standard',
    'stylelint-config-prettier',
    'stylelint-config-recommended-less',
    'stylelint-config-standard-vue'
  ],
  plugins: ['stylelint-order'],
  // 不同格式的文件指定自定义语法
  overrides: [
    {
      files: ['**/*.(less|css|vue|html)'],
      customSyntax: 'postcss-less'
    },
    {
      files: ['**/*.(html|vue)'],
      customSyntax: 'postcss-html'
    }
  ],
  ignoreFiles: [
    '**/*.js',
    '**/*.jsx',
    '**/*.tsx',
    '**/*.ts',
    '**/*.json',
    '**/*.md',
    '**/*.yaml'
  ],
  rules: {
    'no-descending-specificity': null, // 禁止在具有较高优先级的选择器后出现被其覆盖的较低优先级的选择器
    'selector-pseudo-element-no-unknown': [
      true,
      {
        ignorePseudoElements: ['v-deep']
      }
    ],
    'selector-pseudo-class-no-unknown': [
      true,
      {
        ignorePseudoClasses: ['deep']
      }
    ],
    // 指定样式的排序
    'order/properties-order': [
      'position',
      'top',
      'right',
      'bottom',
      'left',
      'z-index',
      'display',
      'justify-content',
      'align-items',
      'float',
      'clear',
      'overflow',
      'overflow-x',
      'overflow-y',
      'padding',
      'padding-top',
      'padding-right',
      'padding-bottom',
      'padding-left',
      'margin',
      'margin-top',
      'margin-right',
      'margin-bottom',
      'margin-left',
      'width',
      'min-width',
      'max-width',
      'height',
      'min-height',
      'max-height',
      'font-size',
      'font-family',
      'text-align',
      'text-justify',
      'text-indent',
      'text-overflow',
      'text-decoration',
      'white-space',
      'color',
      'background',
      'background-position',
      'background-repeat',
      'background-size',
      'background-color',
      'background-clip',
      'border',
      'border-style',
      'border-width',
      'border-color',
      'border-top-style',
      'border-top-width',
      'border-top-color',
      'border-right-style',
      'border-right-width',
      'border-right-color',
      'border-bottom-style',
      'border-bottom-width',
      'border-bottom-color',
      'border-left-style',
      'border-left-width',
      'border-left-color',
      'border-radius',
      'opacity',
      'filter',
      'list-style',
      'outline',
      'visibility',
      'box-shadow',
      'text-shadow',
      'resize',
      'transition'
    ]
  }
}
```
* `package.json`文件script中添加`"lint:style": "stylelint \"./**/*.{css,less,vue,html}\" --fix"`
## Git钩子工具
### 安装插件
```shell
pnpm add husky -D
```


