module.exports = {
    root: true,
    env: {
        browser: true,
        node: true,
        es2022: true,
        'vue/setup-compiler-macros': true
    },
    parser: 'vue-eslint-parser',
    parserOptions: {
        parser: '@typescript-eslint/parser',
        ecmaVersion: 2022,
        sourceType: 'module',
        extraFileExtensions: ['.vue']
    },
    extends: [
        'plugin:vue/vue3-recommended',
        'eslint:recommended',
        '@vue/eslint-config-typescript/recommended'
    ],
    rules: {
        'vue/multi-word-component-names': 'off',
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/no-unused-vars': ['warn', {
            argsIgnorePattern: '^_',
            varsIgnorePattern: '^_'
        }]
    },
    ignorePatterns: ['src/assets/fonts/iconfont.js']
};