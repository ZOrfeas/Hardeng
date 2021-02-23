#!/usr/bin/env node

require('dotenv').config();

const axios = require("axios");

axios.defaults.baseURL = process.env.BASE_URL;

require('yargs/yargs')(process.argv.slice(2))
  .commandDir('scope')
  .demandCommand(1)
  .strict()
  .help()
  .argv