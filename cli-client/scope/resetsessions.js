const axios = require("axios");
const { errorHandler } = require("../utils");

exports.command = 'resetsessions'

exports.desc = 'Initialize charging events & default admin account'

exports.handler = function(argv) {
    
    axios.post('/admin/resetsessions', {
        headers: {
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