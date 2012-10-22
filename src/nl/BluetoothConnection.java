package niekjah;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothConnection {

    private static BluetoothConnection instance = null;
    private StreamConnection stream = null;
    private final String url = "btspp://0050C27FEFA9:1";
    private String DatafrmServer = "";
    OutputStream oustream = null;
    InputStream istream = null;

    public static BluetoothConnection getInstance() {

        if (instance == null) {
            instance = new BluetoothConnection();
        }
        
        return instance;
    }

    public void CreateNewConnection() throws IOException {
        try {
            stream = (StreamConnection) Connector.open(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String WriteBtData(String Data) throws IOException {
        System.err.println(Data + "Writtings");

        if (stream != null) {
            DatafrmServer = "";
            int enddata = 255;
            oustream = stream.openOutputStream();
            oustream.write(Data.getBytes());
            oustream.write(enddata);
            oustream.close();
            istream = stream.openInputStream();
            int ch = 255;
            while ((ch = istream.read()) != enddata) {
                DatafrmServer += (char) ch;
            }
            istream.close();
        }
        return DatafrmServer;
    }

    public void CloseOpenConnection() {
        try {
            if (stream != null) {
                stream.close();
            }
            stream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
