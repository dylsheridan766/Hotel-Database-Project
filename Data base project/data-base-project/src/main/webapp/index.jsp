<html>
<body>
    <h2>Hotel System</h2>
    
    <form action="testdb" method="GET">
        <input type="hidden" name="action" value="list">
        <button type="submit">View All Hotels</button>
    </form>

    <h3>Recherche des chambre par criteres</h3>
<form action="testdb" method="GET">
    <input type="hidden" name="action" value="search">

    <div>
        <input type="checkbox" name="useDate" value="true"> Filter by Date:
       Date de départ: <input type="text" name="sDateVal" placeholder="format YYYY/MM/DD">
       Date de finisement: <input type="text" name="eDateVal" placeholder="format YYYY/MM/DD">
    </div>

    <div>
        <input type="checkbox" name="useCap" value="true"> Filter by Capacity:
        <input type="number" name="capVal" placeholder="e.x. 5">
    </div>
    <div>
        <input type="checkbox" name="useSup" value="true"> Filter by Floor Space:
        <input type="number" name="superVal" placeholder="e.x. 350">
    </div>
    <div>
         <input type="checkbox" name="useChain" value="true"> Filter by Chain:
         <input type="text" name="chainVal" placeholder="e.x. MGM Resorts">
    </div>
    <div>
         <input type="checkbox" name="useCat" value="true"> Filter by Catergory (Stars):
         <input type="number" name="CatVal" placeholder="e.x. 4">
    </div>
    <div>
         <input type="checkbox" name="usePrice" value="true"> Filter by Price:
         <input type="number" name="PriceVal" placeholder="e.x. 400">
    </div>



    <button type="submit">Continue</button>
</form>

</body>
</html>