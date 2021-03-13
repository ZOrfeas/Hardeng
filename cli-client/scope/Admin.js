const axios = require("axios");
const FormData = require("form-data");
const { builder } = require("./SessionsPerProvider");
const qs = require('querystring');
const fs = require('fs');
const { tokenFileExists, createTokenFile, errorHandler, getToken } = require("../utils");

exports.command = 'Admin'

exports.desc = "System Administration"

exports.builder = {
  usermod:
  {
    describe: "Create user or change user's password\n'--usermod --username <username> --passw <Password> --email<Email> --name<User's name>'",
    
  },
  username: {
    describe: "Username"
  },
  passw: {
    describe: "Password"
  },
  email: {
    describe: "User's email"
      
  },
  driverName:{
    describe: "User's name"
  },
  users:
  {
    describe: "User's Activity"
  },
  sessionupd:
  {
    describe: "Add new sessions from a csv file\n --sessionupd --source <Filename.csv>"
  },
  source: {
    describe: "File to upload"
  },
  healthcheck:
  {
    describe: "Check user-database connection"
  },
  resetsessions:
  {
    describe: 'Initialize charging events & default admin account'

  }
}

exports.handler = function(argv)
{
  var args = process.argv.slice(3);
  //console.log(args[0]);
  const token = getToken();
  if(tokenFileExists())
  {
    
    switch(args[0])
    {
      case '--usermod':
        {
          if(args[1] != '--username' || args[3] != '--passw' || args[5] != '--email' || args[7] != '--driverName')
          {
            console.log('Wrong input, please type Admin --help')
          }
          else
          {
            axios.post('/admin/usermod/'+argv.username+'/'+argv.passw,{
              
                driverName: argv.driverName,
                email: argv.email
              },{
              headers: {
                'X-OBSERVATORY-AUTH': token.toString()
              }    
              
            })
            
            .then(res => {
              console.log(res.data);
              console.log(token);
            })
            .catch(err => {
              errorHandler(err);
            })
          }
          break;
        }
      case '--users':
      {
        axios.get('/admin/users/'+argv.users,{
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
        break;
      }
      case '--sessionupd':
        {
          const form = new FormData();
          
          axios.post('/admin/system/sessionupd',form, {
            file: argv.source
          },      
          {
            headers:{
              'X-OBSERVATORY-AUTH': token.toString(),
              'Content-Type': 'multipart/form-data'
            },
            data: form_data
          })
          .then(res => {
            console.log(res.data);
          })
          .catch(err => {
            errorHandler(err);
          })
          break;
        }
      case '--healthcheck':
      {
          axios.get('/admin/healthcheck',{
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
        break;
      }
      case '--resetsessions':
      {
          axios.get('/admin/resetsessions', {
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
        break;
      }
    }
  }else{
    console.log("Please login first");
  }

}