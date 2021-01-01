const axios = require("axios");

exports.command = 'healthcheck'

exports.desc = 'Check user-database connection'

exports.handler = function(argv) {
    
    axios.get('/admin/healthcheck', {
        params: {
            format: argv.format,
            apikey: argv.apikey
        }
    })
    .then(res => {
        console.log(res.data);
    })
    .catch(err => {
        console.log(err.response.data);
    })
}