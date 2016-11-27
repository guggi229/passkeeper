function result(hash){    
	$("#pwdForm:password").val(hash);
	$("#pwdForm").submit();
}


function crypt()
{
	var salt;
	try
	{
		salt = gensalt(13);
	}
	catch(err) 
	{
		alert(err);
		return;
	}
	try
	{
		hashpw($("#pwdForm:password").val(), salt, result);
	} 
	catch(err) 
	{
		alert(err);
		return;
	}

}

