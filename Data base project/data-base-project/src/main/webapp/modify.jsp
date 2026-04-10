<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
  <h2>Modify the Database</h2>
<form action="testdb" method="GET">
  <div>

    <b>Action:</b>
    <input type="radio" name="action" value="alter"> Alter
    <input type="radio" name="action" value="add"> Add
    <input type="radio" name="action" value="remove"> Remove
 </div>

    <b>Table Name:</b>
    <div><input type="radio" name="table" value="hotelmod"> Hotels   <input type="number" name="category" placeholder="categorie">  <input type="text" name="addresse" placeholder="address"><input type="number" name="manid" placeholder="manager_id"> <input type="text" name="roomnum" placeholder="room number"> <input type="text" name="mail" placeholder="e-mail"> <input type="number" name="phone" placeholder="telephone"> <input type="number" name="chid" placeholder="chain_id"> <input type="text" name="place" placeholder="zone"></div>

    <div><input type="radio" name="table" value="roomod"> Rooms  <input type="number" name="rnum" placeholder="room number"><input type="number" name="prix" placeholder="price"> <input type="text" name="commies" placeholder="commodities"> <input type="number" name="capspace" placeholder="capacity"> <input type="text" name="vue" placeholder="view"> extra bed?<input type="checkbox" name="bedxtra" placeholder="extrabed"> <input type="number" name="space" placeholder="size"> <input type="text" name="state" placeholder="condition"> room available?<input type="checkbox" name="vacant" placeholder="status"></div>
<div><input type="radio" name="table" value="empmod"> Employees <input type="text" name="fname" placeholder="full name">  <input type="text" name="empadd" placeholder="address"> <input type="number" name="sin" placeholder="NAS"> <input type="text" name="job" placeholder="role"> <input type="number" name="hotid" placeholder="hotel_id"></div>
<div><input type="radio" name="table" value="empmod"> Clients   <input type="text" name="cname" placeholder="name">  <input type="text" name="cadd" placeholder="address"><input type="number" name="csin" placeholder="NAS"></div>
<input type="hidden" name="action" value="datamod">
    <button type="submit">Execute</button>
   <button onclick="history.back()">Retourner</button>
</form>


</html>

