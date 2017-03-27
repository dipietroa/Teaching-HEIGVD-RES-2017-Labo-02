package ch.heigvd.res.labs.roulette.net.client;



import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import ch.heigvd.schoolpulse.TestAuthor;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.util.List;
import java.util.LinkedList;
import ch.heigvd.res.labs.roulette.data.Student;

/**
 * This class contains automated tests to validate the client and the server
 * implementation of the Roulette Protocol (version 1)
 *
 * @author Gallouche & Dipietroa
 */
public class RouletteV2DipietroaTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public EphemeralClientServerPair roulettePair = new EphemeralClientServerPair(RouletteV2Protocol.VERSION);
    
    /**
     * Test si le serveur est ON
     */
    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theTestRouletteServerShouldRunDuringTests() throws IOException {
        assertTrue(roulettePair.getServer().isRunning());
    }
<<<<<<< HEAD

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theTestRouletteClientShouldBeConnectedWhenATestStarts() throws IOException {
        assertTrue(roulettePair.getClient().isConnected());
    }

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void itShouldBePossibleForARouletteClientToConnectToARouletteServerV2() throws Exception {
        int port = roulettePair.getServer().getPort();
        IRouletteV2Client client = new RouletteV2ClientImpl();
        assertFalse(client.isConnected());
        client.connect("localhost", port);
        assertTrue(client.isConnected());
    }

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theServerShouldReturnTheCorrectVersionNumber() throws IOException {
        assertEquals(RouletteV2Protocol.VERSION, roulettePair.getClient().getProtocolVersion());
    }

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theServerShouldHaveZeroStudentsAtStart() throws IOException {
        int port = roulettePair.getServer().getPort();
        IRouletteV2Client client = new RouletteV2ClientImpl();
        client.connect("localhost", port);
        int numberOfStudents = client.getNumberOfStudents();
        assertEquals(0, numberOfStudents);
    }

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theServerShouldStillHaveZeroStudentsAtStart() throws IOException {
        assertEquals(0, roulettePair.getClient().getNumberOfStudents());
    }

    @Test
    @TestAuthor(githubId = {"Gallouche","dipietroa"})
    public void theServerShouldCountStudents() throws IOException {
        IRouletteV2Client client = roulettePair.getClient();
        assertEquals(0, client.getNumberOfStudents());
        client.loadStudent("sacha");
        assertEquals(1, client.getNumberOfStudents());
        client.loadStudent("olivier");
        assertEquals(2, client.getNumberOfStudents());
        client.loadStudent("fabienne");
        assertEquals(3, client.getNumberOfStudents());
=======
    
    /**
     * Envoie un étudiant au serveur, test si le nombre d'étudiants est 1,
     * effectue un clear, test si le nombre d'étuidants est maintenant 0
     * @throws IOException 
     */
    @Test
    @TestAuthor(githubId = {"Gallouche", "dipietroa"})
    public void shouldHaveNoStudentAfterClear() throws IOException{
        IRouletteV2Client client = new RouletteV2ClientImpl();
        final int port = roulettePair.getServer().getPort();
        client.connect("localhost", port);
        client.loadStudent("Di Pietro Adrian");
        assertEquals(1, client.getNumberOfStudents());
        client.clearDataStore();
        assertEquals(0, client.getNumberOfStudents());
        client.disconnect();
    }
    
    /**
     * Test, après envoi d'une liste d'étudiants au serveur, si
     * listStudents() renvoie cette même liste
     * @throws IOException 
     */
    @Test
    @TestAuthor(githubId = {"Gallouche", "dipietroa"})
    public void serverShouldSendAListOfAllStudents() throws IOException{
        IRouletteV2Client client = new RouletteV2ClientImpl();
        final int port = roulettePair.getServer().getPort();
        client.connect("localhost", port);
        List<Student> expected = new LinkedList<>();
        expected.add(new Student("Di Pietro Adrian"));
        expected.add(new Student("Gallandat Théo"));
        
        client.loadStudents(expected);
        List<Student> returned = client.listStudents();
        
        //Les listes doivent être dans le même ordre pour que le test passe.
        //La liste renvoyée par le serveur devra respecter l'ordre d'insertion 
        //des étudiants.
        assertEquals(expected, returned);
>>>>>>> ae6fc159b58368f18e07f6c5a81d5035b97e5d85
    }
}
