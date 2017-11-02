package com.aubet.francois.pisectr;

import android.app.Instrumentation;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCoPi, btnCoWifi, btnCoWeb, btnCoIPSEC;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
    static ConnectedFeedback theFeedback;
    static SocketManager sock;

    EditText ssid = null;
    EditText pass = null;

    State theState = new State();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


		System.out.println("trying");

		sock = new SocketManager(queue,"192.168.0.53", 20009);
		sock.start();

        //sendCommand("getWIFI");
        try {
            Thread.sleep(100);
        }catch(InterruptedException e){}
        //System.out.println("rep:" + answer);

        /*try {
        sock.join();
        }catch(InterruptedException e){}*/

        btnCoPi = (Button) findViewById(R.id.button_copi);
        btnCoPi.setOnClickListener(this);

        btnCoWifi = (Button) findViewById(R.id.button_cowi);
        btnCoWifi.setOnClickListener(this);

        btnCoWeb = (Button) findViewById(R.id.button_coweb);
        btnCoWeb.setOnClickListener(this);

        btnCoIPSEC = (Button) findViewById(R.id.button_cosec);
        btnCoIPSEC.setOnClickListener(this);

        ssid = (EditText)findViewById(R.id.editWifiSSID);
        pass = (EditText)findViewById(R.id.editWifiPass);

        theFeedback = new ConnectedFeedback();
        theFeedback.start();

		System.out.println("end");
    }



    public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button_copi:
                if(!State.connectedPi) {
                    sock.interrupt();
                    sock = new SocketManager(queue, "192.168.0.24", 20009);
                    sock.start();
                }
                break;
            case R.id.button_cowi:
                    sendCommand("startWIFI");
                    State.wifiSSID = ssid.getText().toString();
                    State.wifiPass = pass.getText().toString();
                    sendCommand(State.wifiSSID);
                    sendCommand(State.wifiPass);
                break;
            case R.id.button_coweb:
                    sendCommand("getWEB");
                break;

            case R.id.button_cosec:
                    sendCommand("getIPSEC");
                break;

            default:
                break;
            }
    }







    public static void onReceivedCommand(String com) {
        switch (com) {
            case "WIFItrue":
                State.connectedWifi = true;
                break;
            case "WIFIfalse":
                State.connectedWifi = false;
                break;
            case "WEBtrue":
                State.connectedWeb = true;
                System.out.println("here");
                break;
            case "WEBfalse":
                State.connectedWeb = false;
                break;
            case "IPSECtrue":
                State.connectedIPSEC = true;
                break;
            case "IPSECfalse":
                State.connectedIPSEC = false;
                break;

            default:
        }

    }

    private void sendCommand(String com) {
        while (!queue.offer(com)) {
            queue.poll();
        }
    }





    	//+++++++++++++++++++++			to show the connection feedbacks			+++++++++++++++++++++++
	private class ConnectedFeedback extends Thread {
		int i = 0;
		Instrumentation inst = new Instrumentation();


		public ConnectedFeedback() {
		}


		@Override
		public void run() {
			boolean a = false;

			while (true) {
				//onResume();

				try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(com.aubet.francois.pisectr.State.connectedPi) {
                                //btnCoIPSEC.setHighlightColor(Color.GREEN);
                                btnCoPi.setBackgroundColor(Color.GREEN);
                            } else {
                                btnCoPi.setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(com.aubet.francois.pisectr.State.connectedWifi) {
                                btnCoWifi.setBackgroundColor(Color.GREEN);
                                btnCoWifi.setText(R.string.btn_cowi_done);
                            } else {
                                btnCoWifi.setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(com.aubet.francois.pisectr.State.connectedWeb) {
                                btnCoWeb.setBackgroundColor(Color.GREEN);
                            } else {
                                btnCoWeb.setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(com.aubet.francois.pisectr.State.connectedIPSEC) {
                                btnCoIPSEC.setBackgroundColor(Color.GREEN);
                            } else {
                                btnCoIPSEC.setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    });

				} catch (Exception e) {}

				try {
					Thread.sleep(390);
				} catch (Exception e) {
				}

			}

		}

	}




}
