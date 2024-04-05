public class WheelchairControlActivity extends Activity implements BluetoothGattCallback {

    private Button btnForward, btnBackward, btnLeft, btnRight, btnStop, btnMic;
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothGatt btGatt;

    // Replace with the UUIDs of your specific Bluetooth service and characteristic for the wheelchair
    private static final String SERVICE_UUID = "YOUR_SERVICE_UUID"; // Replace with actual UUID
    private static final String CHARACTERISTIC_UUID = "YOUR_CHARACTERISTIC_UUID"; // Replace with actual UUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_control);

        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnLeft = findViewById(R.id.btnLeft); 
        btnRight = findViewById(R.id.btnRight);
        btnStop = findViewById(R.id.btnStop);
        btnMic = findViewById(R.id.btnMic);


        btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        // Set button click listeners for movement and microphone (if using voice commands)
            btnForward.setOnClickListener(new View.OnClickListener() {
                   @Override
               public void onClick(View v) {
                     sendCommand("FORWARD"); // Replace with actual command string
               }
           });

btnBackward.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendCommand("BACKWARD"); // Replace with actual command string
    }
});

btnLeft.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendCommand("LEFT"); // Replace with actual command string
    }
});

btnRight.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendCommand("RIGHT"); // Replace with actual command string
    }
});

btnStop.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendCommand("STOP"); // Replace with actual command string
    }
});

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement speech recognition to capture voice commands (optional)
               if (!SpeechRecognizer.isRecognitionAvailable(WheelchairControlActivity.this)) {
            // Speech recognition not supported on this device
            return;
        }

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(WheelchairControlActivity.this);

        // Intent for speech recognition (optional parameters)
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your command");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                // Speech recognizer is ready for user input
            }

            @Override
            public void onBeginningOfSpeech() {
                // Speech has begun
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Monitor the root mean square decibel level (optional)
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Handle speech recognition events (optional)
            }

            @Override
            public void onResults(Bundle results) {
                // Speech recognition results are available
                ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults != null && voiceResults.size() > 0) {
                    String recognizedCommand = voiceResults.get(0); // Get the first result (highest confidence)
                    // Process the recognized command (e.g., send it to the wheelchair)
                    handleVoiceCommand(recognizedCommand);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Handle partial results during speech recognition (optional)
            }

            @Override
            public void onError(int error) {
                // Speech recognition error (e.g., network error, no speech detected)
                handleError(error);
            }
        });

        speechRecognizer.startListening(speechRecognizerIntent);
    }
});

    @Override
    protected void onResume() {
        super.onResume();
        // Connect to Bluetooth device (replace with placeholder for your implementation)
        if (btAdapter != null && btDevice != null) {
           (btAdapter != null && btDevice != null) {
            btGatt = btDevice.connectGatt(this, false, this);
        }
    }

    // Implement BluetoothGattCallback methods for connection, services discovery, 
    // characteristic write, etc.

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            // Connected! Discover services
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // Disconnected
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // Get the service
            BluetoothGattService service = gatt.getService(UUID.fromString(SERVICE_UUID));
            if (service != null) {
                // Get the characteristic
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID));
                if (characteristic != null) {
                    private void sendCommand(String command) {
    if (btGatt == null || characteristic == null) {
        // Handle error: Not connected or characteristic not found
        return;
    }

    // 1. Prepare Command Data (Convert to Byte Array)
    byte[] data = command.getBytes(); // Assuming your commands are simple strings

    // 2. Set Characteristic Value
    characteristic.setValue(data);

    // 3. Write the Data to the Characteristic
    boolean writeSuccess = btGatt.writeCharacteristic(characteristic);

    // 4. Handle Write Result (Optional)
    if (writeSuccess) {
        // Characteristic write successful
        // (You can add logic here to handle successful writes, e.g., display confirmation)
    } else {
        // Characteristic write failed
        // Handle the error (e.g., display error message, retry)
    }
}
                }
            }
        }
    }

    private void sendCommand(String command) {
        if (btGatt == null || characteristic == null) {
            // Handle error: Not connected or characteristic not found
            return;
        }
        // Prepare the command data (e.g., convert to byte array)
        byte[] data = command.getBytes();
        characteristic.setValue(data);
        // Write the data to the characteristic
        btGatt.writeCharacteristic(characteristic);
    }
    
            
