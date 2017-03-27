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
    LOG.info(r.readLine());
  }

  @Override
  public void disconnect() throws IOException {
    LOG.info("IN DISCONNECT()");
      if(this.isConnected()){
        w.println(RouletteV1Protocol.CMD_BYE);
        w.flush();
        socket.close();
        w.close();
        r.close();
    }
    else
        throw new IOException("Client already disconnected");
    
  }

  @Override
  public boolean isConnected() {
      LOG.info("IN ISCONNECTED()");
      return socket != null && socket.isConnected();
  }

  @Override
  public void loadStudent(String fullname) throws IOException {
    LOG.info("IN LOADSTUDENT()");
      if(this.isConnected()){
        w.println(RouletteV1Protocol.CMD_LOAD);
        w.flush();
        
        if(!r.readLine().equals(RouletteV1Protocol.RESPONSE_LOAD_START))
            throw new IOException();
        
        w.println(fullname);
        w.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        
        if(!r.readLine().equals(RouletteV1Protocol.RESPONSE_LOAD_DONE))
            throw new IOException();
    }
    else
        throw new IOException("Client not connected");
  }

  @Override
  public void loadStudents(List<Student> students) throws IOException {
      LOG.info("IN LOADSTUDENTS()");
      if(this.isConnected()){
        w.println(RouletteV1Protocol.CMD_LOAD);
        if(!r.readLine().equals(RouletteV1Protocol.RESPONSE_LOAD_START))
          throw new IOException();

        for(Student s : students){
            w.println(s);
        }

        w.println(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);
        if(!r.readLine().equals(RouletteV1Protocol.RESPONSE_LOAD_DONE))
            throw new IOException();
      }
      else
        throw new IOException("Client not connected");
  }

  @Override
  public Student pickRandomStudent() throws EmptyStoreException, IOException {
      LOG.info("IN PICKRANDOMSTUDENT()");
      if(this.isConnected()){
          w.println(RouletteV1Protocol.CMD_RANDOM);
          
          try{
              Student randStudent = JsonObjectMapper.parseJson(r.readLine(), Student.class);
              w.flush();
              return randStudent;
          }catch(IOException e){
              throw new EmptyStoreException();
          }
        
      }
      throw new IOException();
  }

  @Override
  public int getNumberOfStudents() throws IOException {
      LOG.info("IN GETNUMBEROFSTUDENTS()");
      if(this.isConnected()){
          w.println(RouletteV1Protocol.CMD_INFO);
          InfoCommandResponse icr = JsonObjectMapper.parseJson(r.readLine(), InfoCommandResponse.class);
          w.flush();
          return icr.getNumberOfStudents();   
      }
      throw new IOException("Client not connected");
  }

  @Override
  public String getProtocolVersion() throws IOException {
      LOG.info("IN GETPROTOCOLVERSION()");
      return RouletteV1Protocol.VERSION;
  }

}
