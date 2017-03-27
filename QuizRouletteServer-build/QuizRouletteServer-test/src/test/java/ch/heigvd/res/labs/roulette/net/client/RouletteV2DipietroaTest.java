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
    public void serverShouldRun(){
        assertTrue(roulettePair.server.isRunning());
    }
    
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
    }
}
