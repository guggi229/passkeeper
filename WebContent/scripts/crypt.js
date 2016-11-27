function encode(control)
{
    var ckey = $("#cryptonkey").val();
    var y=document.getElementById(control).value;
    var encrypted = CryptoJS.AES.encrypt(y, ckey);
    document.getElementById("crypton").value=encrypted;
}

function decode(control)
{
    var ckey = $("#cryptonkey").val();
    var y=document.getElementById(control).value;
    var decrypted = CryptoJS.AES.decrypt(y, ckey);
    decrypted = decrypted.toString(CryptoJS.enc.Utf8);
    document.getElementById("cryptoff").value=decrypted;
}
