Installations instruktioner:
Du behöver ha IntelliJ, MongoDB compass och postman installerat innan du börjar
Datan som jag använde är:
Users:
```
    {
        "username": "Abbe93",
        "password": "123456",
        "phone": "+46707123456",
        "email": "abbe@email.com",
        "adress": "Arödsgatan 15"
    },
    {
        "username": "dorisPlantLover",
        "password": "ILovePlants",
        "phone": "+46737102030",
        "email": "plantlover@email.com",
    }
```
       
Plants (gjorde flera av dem när det behövdes):
```
{
    "user": {
        "id": "679cd0674f8fca2b99c46e24",
        "username": "dorisPlantLover",
        "password": "ILovePlants",
        "phone": "+46737102030",
        "email": "plantlover@email.com",
        "adress": "Plantgatan 01"
    },
    "name": "Kaktusväxt",
    "scientificName": "Cactaceae",
    "age": 2,
    "size": "12 cm",
    "type": "kaktus",
    "lightNeeds": "Mycket",
    "waterNeeds": "Lite",
    "difficulty": 2,
    "trade": false,
    "price": 350.0,
    "pictureUrl": "https://kaktusbild.org/",
    "status": "available"
},
{
    "user": {
        "id": "679cd0674f8fca2b99c46e24",
        "username": "dorisPlantLover",
        "password": "ILovePlants",
        "phone": "+46737102030",
        "email": "plantlover@email.com",
        "adress": "Plantgatan 01"
    },
    "name": "Ros",
    "scientificName": "Rosa rubiginosa",
    "age": 1,
    "size": "10 cm",
    "type": "Ros",
    "lightNeeds": "Mycket",
    "waterNeeds": "En del",
    "difficulty": 3,
    "trade": true,
    "price": 100.0,
    "pictureUrl": "https://Rosbild.org/",
    "status": "available"
}
```
Transaction mall:
```
{
    "user": {
        "id":
    },
    "plant": {
        "id":
    }
    "tradePlant{
        "id":
    }
}
```

Postman länk:
https://documenter.getpostman.com/view/40863104/2sAYX5MNuM

Affärsregler:

En användare kan inte ha mer än 10 aktiva annonser samtidigt:
Detta löste jag genom att när en planta skapas, så görs en lista av den användaren plantor. 
Sedan med en for loop kollar jag igneom listan för att se alla som är avialable, och gör en int som ökas med ett. 
Sedan ser jag till att den int:en inte är strörre än 10

Växter markerade för byte kan endast bytas mot andra växter, inte säljas:
Den här försökte jag lösa med en if-sats. Jag har inte möjlighet att testa detta då min plantors information försvinner när dem läggs in i transaction

Vid byte måste båda parter godkänna bytet innan det genomförs:
Detta simullerade jag genom att se till att när en trade genomförs så sätts dem i "pending, awaiting approval". Detta kunde jag inte testa pågrund av samma fel som åvan.

Prissatta växter måste ha ett fast pris mellan 50-1000 kr:
Detta villa jag lösa med @Min och @Max. Dock fungerade inte importen som behövdes för att dem skulle funka, så dem ligger inne men är bort kommenterade.

Lista över begränsningar:
Koden är inte validerad, så man kan skriva lite som man vill
Vissa saker fungerar inte som jag har beskrivit åvan.
har inget sätt att gå från pending till traded i en transaction
Saker som status är en string, vilket gör det svårare att använda dem då det kan stå lite vad som helst
ingen säkerhet

Förslag på förbättringar:
Det som jag skulle göra är att se till att importen fungerar för @Min och @Max
Fixa så att plants infon inte blir null så fort dem lägga in i transaction.
Göra status, type och size på Plants till enum, för att kunna bättre bestämma vad dem ska vara.
Skulle inte använda @Autowired, utan det rätta sättet
Använt en service class för att snygga till det hela lite
Validera koden med t.ex NotNull, NotEmpty
Lägga till så att man kan bekräfta en trade
Lägga till funktioner som har med säkerhet att göra, för att göra det säkrare
