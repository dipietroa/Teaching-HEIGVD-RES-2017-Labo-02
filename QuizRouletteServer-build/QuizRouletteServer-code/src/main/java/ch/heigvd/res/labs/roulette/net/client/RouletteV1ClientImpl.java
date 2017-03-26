package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RandomCommandResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the client side of the protocol specification (version 1).
 * 
 * @author Olivier Liechti
 */
public class RouletteV1ClientImpl implements IRouletteV1Client {

  private static final Logger LOG = Logger.getLogger(RouletteV1ClientImpl.class.getName());
  Socket socket = null;
  BufferedReader r;
  PrintWriter w;
  
  @Override
  public void connect(String server, int port) throws IOException {
    socket = new Socket(server, port);
    r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    w = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    r.readLine();
  }

  @Override
  public void disconnect() throws IOException {
    if(socket != null){
        w.println(RouletteV1Protocol.CMD_BYE);
        w.flush();
        r.readLine();
    }
  }

  @Override
  public boolean isConnected() {
      return socket != null;
  }

  @Override
  public void loadStudent(String fullname) throws IOException {
    if(socket != null){
        w.println(RouletteV1Protocol.CMD_LOAD);
        r.readLine();
        w.println(fullname);
        w.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        w.flush();
        r.readLine();
    }
  }

  @Override
  public void loadStudents(List<Student> students) throws IOException {
      if(socket != null){
          w.println(RouletteV1Protocol.CMD_LOAD);
          r.readLine();
          
          for(Student s : students){
              w.println(s);
          }
          
          w.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
          w.flush();
          r.readLine();
      }
  }

  @Override
  public Student pickRandomStudent() throws EmptyStoreException, IOException {
      if(socket != null){
          w.println(RouletteV1Protocol.CMD_RANDOM);
          w.flush();
          
          try{
              Student randStudent = JsonObjectMapper.parseJson(r.readLine(), Student.class);
              return randStudent;
          }catch(IOException e){
              throw new EmptyStoreException();
          }
      }
      throw new IOException();
  }

  @Override
  public int getNumberOfStudents() throws IOException {
      if(socket != null){
          w.println(RouletteV1Protocol.CMD_RANDOM);
          w.flush();
          InfoCommandResponse icr = JsonObjectMapper.parseJson(r.readLine(), InfoCommandResponse.class);
          return icr.getNumberOfStudents();   
      }
      throw new IOException();
  }

  @Override
  public String getProtocolVersion() throws IOException {
      return RouletteV1Protocol.VERSION;
  }



}
