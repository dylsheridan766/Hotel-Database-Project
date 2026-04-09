<html>
<style>
    body {
      font-family: 'Segoe UI', sans-serif;
    }
  </style>
 <h3>Client Sign Up</h3>

        <form>
            <div>
                Name: <input type="text" name="name" placeholder="e.x. Jane Doe">
            </div>
            <div>
                Address: <input type="text" name="address" placeholder="e.x. 613 park avenue">
            </div>
            <div>
                E-mail: <input type="text" name="email" placeholder="e.x. scientologytoday@gmail.com">
            </div>
            <div>
                NAS: <input type="number" name="NAS" placeholder="e.x. 123456789">
            </div>
            <form action="testdb" method="GET">
                    <input type="hidden" name="action" value="append">
                    <button type="submit">Soumettre</button>
            </form>
           <button onclick="history.back()">Retourner</button>

        </form>
</html>