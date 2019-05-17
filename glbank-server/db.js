const mysql = require('mysql');

const con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root",
    database: "glbankmart"
    });
  
con.connect(function(err) {
    if (err) throw err;
    console.log("Database connected!");
    });

module.exports = {

    getLoginData(username, password, queryResult)
    {
      const query = "select * from loginclient where login =\'" + username + "\' and password = MD5(\'" + password + "'\);";
      con.query(query, (err, result) =>{
           if (err)
           {
             console.log(err);
             queryResult("Error");
           }
           else
           {
             queryResult(JSON.stringify(result));
           }
      });
    },

    getLoginByUsername(username, queryResult)
    {
      const query = "select id from loginclient where login = \'" + username + "\';";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getLastThreeRecords(idc, queryResult)
    {
      const query = "select * from loginhistory where idl = (select id from loginclient where idc = " + idc + ")order by UNIX_TIMESTAMP(logDate) desc limit 3;";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    writeLoginAttempt(idl, success)
    {
      const query = "insert into loginhistory(idl, Success) VALUES (" + idl + ", " + success + ");";
      con.query(query, err => {
        if (err)
        {
          console.log(err);
        }
        else
        {
          console.log("Record successfully inserted");
        }
      });
    },

    getClientInfo(idc, queryResult)
    {
      const query = "select * from client where id = "+idc+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getAccounts(idc, queryResult)
    {
      const query = "select AccNum from account where idc = "+idc+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getAccountInfo(accNum, queryResult)
    {
      const query = "select id, amount from account where AccNum = \'"+accNum+"\';";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getTransHistory(idAcc, queryResult)
    {
      const query = "select TransAmount, idAcc from transaction where idAcc = "+idAcc+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getCards(idAcc, queryResult)
    {
      const query = "select id from card where ida = "+idAcc+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getCardInfo(idCard, queryResult)
    {
      const query = "select Active, ExpireM, ExpireY from card where id = "+idCard+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    },

    getCardTrans(idCard, queryResult)
    {
      const query = "select TransAmount, TransDate from cardtrans where idCard = "+idCard+";";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
      });
    }

};