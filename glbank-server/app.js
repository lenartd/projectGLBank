let database = require('./db');
const app = require('express')();
const bodyParser = require("body-parser");

app.use(bodyParser.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });  



app.post('/login', (req, res) => {

    database.getLoginData(req.body.name, req.body.password, queryResult =>{
        console.log(queryResult);
    });

    return res.status(200).send();
  });

app.listen(3125, () =>
  console.log('Server started on port 3125'),
);