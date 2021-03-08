const axios = require("axios");
const { errorHandler, getToken } = require("../utils");

exports.command = 'healthcheck'

exports.desc = 'Check user-database connection'

exports.handler = function(argv) {
    const token = getToken();
    axios.get('/admin/healthcheck', {
        headers:{
            'X-OBSERVATORY-AUTH': token.toString()
        }
    })
    .then(res => {
        console.log(res.data);
    })
    .catch(err => {
        errorHandler(err);
    })
}