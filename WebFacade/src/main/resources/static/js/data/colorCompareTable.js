const axiosInstance = axios.create({
    // 设置全局请求头，解决跨域问题
    headers: {
        // "Content-Type" : "application/x-www.form-urlencoded"
        "Content-Type": "application/octet-stream",
        "Access-Control-Allow-origin": "*"
    },
    baseURL: "http://localhost:8080/",
    timeout: 5000
});

const vm = new Vue(
    {
        // vue 绑定视图，这里通过id绑定
        el: '#colorCompareTablePage',
        data: {
            pi: '',  // 分页信息
            colorCompareDataList: [],
            pageNum: 1,    // 当前页数
            pageSize: 10,    // 每页记录数
            total: 0,   // 数总记录数
        },

        methods: {
            // 查找购物车信息，分页显示
            findByPage: function () {
                const vm = this;

                // 先临时绑定后面做gateway！！！ todo xgf
                axios.post('http://localhost:8080/data/searchColorCompare', {
                    param: {
                    },
                    page: {
                        pageNum: vm.pageNum,
                        pageSize: vm.pageSize
                    }
                }).then(function (response) {
                    // 浏览器调试数据在response.data.data 中
                    vm.colorCompareDataList = response.data.data;
                    // 用于分页
                    vm.pi = response.data.data;
                }).catch(function (err) {// 请求失败
                    console.log("获取颜色对照表信息失败：" + err);
                    alert(err.message);
                });
            }
        },

        // 当vue对象创建完毕之后需要执行的函数（也就是页面加载完成的时候执行）
        created: function () {
            // 查询对应id的购物车信息
            this.findByPage()
        },

    },

);



