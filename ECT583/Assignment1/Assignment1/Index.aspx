<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Index.aspx.cs" Inherits="Assignment1.Index" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
    <form id="form1" runat="server">
    <div class="container">
        <div class="jumbotron">
            <h1>Welcome to Rong Zhuang's website</h1> 
            <p>This page shows you how session works.</p> 
        </div>
        <div class="panel panel-info">
          <div class="panel-heading">Click below 'Refresh' button or F5 from web browser to see different results.</div>
          <div class="panel-body"><asp:Button ID="btnRefresh" runat="server" Text="Refresh" class="btn btn-primary" OnClick="btnRefresh_Click" /></div>
        </div>
        
        <div class="panel panel-default">
          <div class="panel-body">Visited Times: <asp:Label runat="server" ID="lblVisitedTimes"></asp:Label> <br/>
				Message: <asp:Label runat="server" ID="lblMessage"></asp:Label> <br/></div>
        </div>
	</div>
    </form>
</body>
</html>
