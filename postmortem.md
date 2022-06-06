# Java Chattprogram

Filip TE19 2022

## Inledning

Målet var att göra ett chattprogram där inloggade användare kan skriva till varandra genom en server som 
sköter inloggning och användarnamn och skickandet av meddelanden. Varje client har en connection till en 
server och servern har en connection till alla användare. Jag skrev två program med samma model men olika 
controllers och client programmet har en view medan servern måste köras genom en terminal. 

## Bakgrund

Jag började med att kopiera över det MVC Network program som jag hade gjort tidigare och gjorde en enkel 
GUI för clientprogrammet med Java swing. Min planering var i princip att få alla features som jag hade 
lagt till i view att fungera. 

* Jag skulle kunna skicka och ta emot meddelanden från servern och andra 
användare. 
* Jag skulle kunna skapa en ny användare och logga in. 
* Flera clients skulle kunna connecta och chatta samtidigt.

Så, en ganska enkel planering eftersom 
jag visste vad som skulle göras. Därför kändes det onödigt 
att skriva ner planeringen i en separat fil.

Jag började med att lägga till actionlisteners till knapparna i clientviewen och gjorde så att send 
använder send funktionen som fanns i model sen innan för att skicka meddelandet i view till servern. 
Jag gjorde också så att när servern skickar meddelanden till client så är det en thread som tar emot 
dem och lagrar dem i en kö som controller läser från och skickar till view. Basic read/write mellan 
server och client.

Sen gjorde jag så att server kan ta meddelanden från alla client connections och sen skickar ut dem. 
Då kan en client skicka ett meddelande och alla andra connectade till servern ser det.

Sen laddade jag ner BCrypt för Java för att hasha lösenord innan client skickar det till server. 
Client skickar en speciell sträng med all information om inloggningen som servern tolkar speciellt 
eftersom den börjar med "/login" 

Sen gjorde jag så att servern kan kolla lösenordet och användarnamnet mot databasen. Fick problem först 
att det inte blev samma hash av samma lösenord, men fick reda på att det var på grund av att BCrypt 
använde en ny salt varje gång. Så jag genererade en salt med BCrypt som jag hårdkodade in i programmet.

Servern måste veta vem som skickar login och meddelanden så jag gjorde så att 
servern skickar en id till client som den använder för att kommunicera tillbaka till servern. 

Signup var ganska enkelt, jag gjorde samma som vid login för att behandla requesten men skickar 
bara en insert till databasen istället, men måste först kolla om det redan finns en user med samma namn 
i databasen redan, så att den användaren inte blir overwritead.

Sen gick jag igenom programmet för att se till att det följde MVC och skrev kommentarer. Jag flyttade t.ex. 
frame skapandet från controller till view för att det skulle vara lättare att byta ut view mot en annan.

Jag hade fram tills nu haft en class som lagrar variabler för databasinloggning och gjorde gitignore för 
att inte lägga upp den informationen på github. Men jag märkte att när jag gjorde en .jar fil så 
inkluderades ju den filen som jag inte ville lägga upp på github, så jag gjorde en snabbfix genom att 
istället ta databasinloggningen från användaren vid start av server. Annars inga större problem med .jar 
skapande.

## Positiva erfarenheter

Det gick bra att använda flera threads t.ex. för att servern skulle kunna ta emot meddelanden medan den 
gör saker med databasen. Jag har använt threads förut och det var svårt att förstå det först men nu visste 
jag redan hur jag skulle göra.

Jag tycker att lösningen jag använde för att skicka data från client till server funkade bra. 
Jag började strängen med vad clienten ville så att servern skulle veta vilken data som fanns och vad som 
skulle göras med den. Sen skulle i princip all data behandlas som strängar så därför fungerade det bra 
att skicka hela kommandot som en sträng. Om t.ex. stora mängder siffror skulle behandlas för att jag 
skulle göra ett spel så skulle jag inte lösa det på samma sätt.

Jag fick lära mig att ladda ner och använda BCrypt och mysql connector i mitt program. Det är ju bra att 
kunna använda andras kod för att göra saker lättare, så behöver man inte återuppfinna hjulet 
varje gång man ska göra något.

## Negativa erfarenheter

Jag surroundade readLine med try/catch i listenerthread utan att göra något med det. Då fortsatte bara 
loopen med msg != null och att msg var samma hela tiden så att vid en exception (t.ex. att någon disconnectar) 
så skickades det sista meddelandet om och om igen, men det blev då svårt att debugga eftersom jag hade tagit 
exceptionen som skapade problemet och bara ignorerat den, så jag fick inget error.

Det hade varit smidigare med en .config fil som sparar tidigare databasinloggningsuppgifter så att om man 
startar om servern på samma databas så behöver man inte skriva in alla uppgifter varje gång.

## Sammanfattning

* Har använt threads och det blir lättare och lättare.
* Jag lärde mig att min lösning för att skicka data i ett chattprogram verkar fungera bra och var lämpligt 
för uppgiften.
* Jag lärde mig att ladda ner och använda andras kod.
* Lite mer tanke borde gå åt att tänka på konsekvenserna av att try/catcha och ignorera resultatet men 
också att det kan vara en bra sak att kolla när man debuggar.
* Hade varit bra med en .config fil för att det skulle bli mer användarvänligt.

Det går att utveckla programmet och lägga till nya kommandon relativt enkelt. T.ex. så skulle man kunna 
lägga till ett kommando för att skicka ett privat meddelande till en annan användare med /pm istället 
för /msg och skicka meddelandet till det client id som är kopplat till ett användarnamn om användaren 
är inloggad (jag har ju redan en sån lista). Det skulle också gå att skapa en view för servern 
eller en annan view för client om man skulle vilja det.