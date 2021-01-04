const axios = require("axios");

exports.command = 'Admin'

exports.desc = "System Administration"

exports.builder = {
  usermod:
  {
    describe: "Create user or change user's password",
  },
  users:
  {
    describe: "User's Activity"
  },
  sessionupd:
  {
    describe: "Add new sessions from a csv file"
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
