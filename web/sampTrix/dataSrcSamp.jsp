<%-- 
    Document   : ch6-ex1
    Created on : Feb 12, 2018
    Author     : ajs7477
--%>

<jsp:include page="/inc/Header.jsp?type=mini&title=DataSourceSample"></jsp:include>

<body>
    
    <script src="wxSamp.js"></script>
    <div data-dojo-type="dojo.data.ItemFileReadStore" data-dojo-props="data:ifrs" data-dojo-id="wxSample"></div>
    <div data-dojo-type="dijit.form.Button" data-dojo-id="button2">Find stations!</div>
    <br><br>
    <span id="list2"></span>
</body>

 <jsp:include page="/inc/Footer.jsp"></jsp:include>