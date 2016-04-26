<%-- 
    Document   : guess
    Created on : Apr 25, 2016, 2:23:32 PM
    Author     : RZHUANG
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Guess Number</title>
    </head>
    <body>
        <div align="center">
            <header>
                <h1>Guess a number to get reward</h1>
            </header>
            <div id="body">
                <h2 style="color:red">${errmsg}</h2>
                <div style='width:40%'>
                    <table style='width:80%' border='1px'>
                        <tr><th>Random Number</th><th>Your Guess</th><th>Right Times</th><th>Your Reward</th></tr>
                        <tr align=center><td>${random}</td><td>${guess}</td><td>${times}</td><td>$${reward}</td></tr>
                    </table>
                    <hr/>
                </div>
                <div style='width:40%'>
                    <form action="Guess" method="Post">
                        <table style='width:60%'>
                            <tr><td>Number:</td><td><input type='text' name='number' value='${guess}' required /></td></tr>
                            <tr><td></td><td><input name="Submit" value="Login" type="submit" /></td></tr>
                        </table>
                    </form>
                </div>
            </div>
                <hr/>
            <footer>
                <p>&copy; Middle Term Practice by Rong Zhuang.</p>
            </footer>
        </div>
    </body>
</html>
