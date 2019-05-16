const UIDGenerator = require('uid-generator');

const tokengen = new UIDGenerator(512, UIDGenerator.BASE62);

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
    },

    generateToken(token)
    {
      tokengen.generate().then(uid => token(uid));
    },

    verifyUserToken(userData, tokenArray, verified)
    {
        let state = -1;
        for(let i = 0; i<tokenArray.length; i++)
        {
            if(tokenArray[i].name == userData.body.name && tokenArray[i].token == userData.body.token)
            {
                state = i;
                break;
            }
        }
        verified(state);
    }
};