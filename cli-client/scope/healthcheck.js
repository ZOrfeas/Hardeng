const axios = require("axios");
const { errorHandler } = require("../utils");

exports.command = 'healthcheck'

exports.desc = 'Check user-database connection'

exports.handler = function(argv) {

    axios.get('/admin/healthcheck', null)
    .then(res => {
        console.log(res.data);
    })
    .catch(err => {
        errorHandler(err);
    })
}