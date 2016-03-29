<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Index.aspx.cs" Inherits="Assignment1.Index" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div id="mainBody">
		<header>
			<hgroup>
				<h1>Welcome to this page</h1>
			</hgroup>
		</header>
		<section>
				Visisted Times: <asp:Label runat="server" ID="lblVisistedTimes"></asp:Label> <br/>
				Message: <asp:Label runat="server" ID="lblMessage"></asp:Label> <br/>
		</section>
		<footer>
			<p>Try to refresh this page to see different result.</p>
		</footer>
	</div>
    </form>
</body>
</html>
