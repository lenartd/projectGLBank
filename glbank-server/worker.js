let jwt = require('jsonwebtoken');
const config = require('./config');


let checkToken = (req, result) => {
    let token = req.headers['x-access-token'] || req.headers['authorization'];
    if (token.startsWith('Bearer ')) 
    {
      token = token.slice(7, token.length);
    }
  
    if (token) 
    {
      jwt.verify(token, config.secret, (err, decoded) => {
            if (err) 
            {
                result(false);
            } 
            else
            {
                req.decoded = decoded;
            }
      });
    } 
    else 
    {
      result(false);
    }
  };  


module.exports = {
    checkLoginHistory(history, result)
    {
        let isblocked = false;
        let blocks = 0;
        for(let i =0; i<history.length; i++)
        {
            if ( i == 0 && history[i].Success == null)
            {
                isblocked = true;
            }
            else if ( i == 0 && history[i].Success == 1)
            {
                isblocked = false;
            }
            else if (history[i].Success == null) {}
            else if (history[i].Success == 1) {}
            else {blocks++;}
        }

        if (blocks == 3){isblocked = true;}
        else if(blocks < 3 && isblocked){isblocked = true;}
        else{isblocked = false;}
        result(isblocked);
    }
};