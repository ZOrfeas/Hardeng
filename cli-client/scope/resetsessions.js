const axios = require("axios");

exports.command = 'resetsessions'

exports.desc = 'Initialize charging events & default admin account'

exports.handler = function(argv) {
    
    axios.post('/admin/resetsessions', {
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