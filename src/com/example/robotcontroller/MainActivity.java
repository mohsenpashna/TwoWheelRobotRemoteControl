package com.example.robotcontroller;



import ioio.lib.api.DigitalOutput;
import ioio.lib.api.TwiMaster;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends IOIOActivity {
	private int touchX ;
	private int touchY ;
	private int motorR ;
	private int motorL ;
	private int valR, valL ;
	private byte ValR,ValL;
	private TextView rMotorTX;
	private TextView lMotorTX;
	private Button Exit,Stop;
	private ToggleButton ConnectionTester;
	private DigitalOutput led_;
	private TwiMaster twi;
	//private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		rMotorTX = (TextView)findViewById(R.id.rightMotor);
		lMotorTX = (TextView)findViewById(R.id.leftMotor);
		
		Exit = (Button)findViewById(R.id.exitButton);
	 	Exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{

					motorR = 0;motorL = 0;
					byte[] MotorSpeeds = {127,127};
					motorRun(MotorSpeeds);
					finish();
				}catch(Exception e){
				
				System.exit(1);}
			
			}
		});
		
		Stop = (Button)findViewById(R.id.stopButton);
 		Stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				motorR = 0;motorL = 0;
				byte[] MotorSpeeds = {127,127};
				motorRun(MotorSpeeds);
				}catch(Exception e){};
				}
		});

		ConnectionTester = (ToggleButton)findViewById(R.id.ConnectionTB);
		
		
	}
	

	

	protected void motorRun(byte[] request) {
		// TODO Auto-generated method stub
		if (request==null){
			request =  new byte[] {127,127};
		}
	try{
	byte[] response = new byte[1] ;

			twi.writeRead(4,false, request, request.length, response, 1);
	}catch(InterruptedException e){

	} catch (ConnectionLostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		touchX =  (int) event.getX() - 540;
		touchY =  880 - (int) event.getY() ;

		 motorR = (touchY -  touchX)/3;
		 motorL = (touchY +  touchX)/3;
		
			if (motorR>500){
				motorR=500;
			}
			if (motorL>500){
				motorL=500;
			}
		 
		return super.onTouchEvent(event);
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
class LOOPER extends BaseIOIOLooper{

		
		@Override
		protected void setup() throws ConnectionLostException,
				InterruptedException {
			// TODO Auto-generated method stub
			super.setup();

			led_ = ioio_.openDigitalOutput(0, false);
			twi = ioio_.openTwiMaster(1, TwiMaster.Rate.RATE_100KHz, false);

		}
		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			// TODO Auto-generated method stub
			super.loop();
//			motorR = 0;motorL = 0;
			led_.write(!ConnectionTester.isChecked());
			Thread.sleep(100);
			valR = motorR +127;
			valL = motorL +127;
			
			if 	(valR > 255)		valR = 255;
			if	(valR<0)			valR=0;
			if	(valL > 255)		valL = 255;
			if	(valL<0)			valL=0;
			
			 ValR = (byte) valR;
			 ValL = (byte) valL; 
			
 	byte[]  MotorSpeeds = {ValR,ValL};
			motorRun(MotorSpeeds);
			screenupdater();
}
		
		
		@Override
		public void disconnected() {
			// TODO Auto-generated method stub
			super.disconnected();
			ConnectionTester.setActivated(false);
		}
		@Override
		public void incompatible() {
			// TODO Auto-generated method stub
			super.incompatible();
		}
		
		
		
	}
	
@Override
protected IOIOLooper createIOIOLooper() {
	return new LOOPER();
}



	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) {
		// TODO Auto-generated method stub
		return super.createIOIOLooper(connectionType, extra);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	
private void screenupdater() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
			rMotorTX.setText(String.valueOf(valR));
			lMotorTX.setText(String.valueOf(valL));
			
		}
	});
}
	

		

}
