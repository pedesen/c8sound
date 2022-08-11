package io.camunda;
import java.net.*;
import java.io.*;
import java.util.Optional;
import io.camunda.zeebe.exporter.api.Exporter;
import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.protocol.record.Record;
import io.camunda.zeebe.protocol.record.RecordType;
import io.camunda.zeebe.protocol.record.ValueType;
import io.camunda.zeebe.protocol.record.intent.ProcessInstanceIntent;
import io.camunda.zeebe.protocol.record.value.ProcessInstanceRecordValue;

/* 
 * This exporter filters events and sends messages via UDP protocol
 * 
 * Currently supported ValueTypes: PROCESS_INSTANCE, INCIDENT
 *
 * The message format for process instances events:
 * <Intent> <BpmnElementType>
 * Example: "5 startEvent"
 * 
 * The message for incident events is just: "i"
 *
 * Here goes a list of intents:
 *
 * 0 CANCEL
 * 1 SEQUENCE_FLOW_TAKEN
 *
 * 2 ELEMENT_ACTIVATING
 * 3 ELEMENT_ACTIVATED
 * 4 ELEMENT_COMPLETING
 * 5 ELEMENT_COMPLETED
 * 6 ELEMENT_TERMINATING
 * 7 ELEMENT_TERMINATED
 *
 * 8 ACTIVATE_ELEMENT
 * 9 COMPLETE_ELEMENT
 * 10 TERMINATE_ELEMENT
 */
public class SocketExporter implements Exporter
{
    private DatagramSocket socket;
    private InetAddress address;
    private Controller controller;
  
    private void sendMessage (String message) {
        try {
            byte[] data = message.getBytes("ASCII");
            DatagramPacket packet = new DatagramPacket(data, data.length, this.address, 1337);
            socket.send(packet);
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
    
    public void open(Controller controller) {
        this.controller = controller;
        
        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName("localhost");
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void export(Record record) {
        if (record.getRecordType() == RecordType.EVENT) {
            if(record.getValueType() == ValueType.PROCESS_INSTANCE) {
                ProcessInstanceRecordValue value = (ProcessInstanceRecordValue) record.getValue();
                ProcessInstanceIntent intent = (ProcessInstanceIntent) record.getIntent();
                String bpmnElementType = value.getBpmnElementType().getElementTypeName().get();
                short intentId = intent.getIntent();
                this.sendMessage(intentId + " " + bpmnElementType);
            }
            if(record.getValueType() == ValueType.INCIDENT) {
                System.out.println(record.toJson());
                this.sendMessage("i");
            }
        }

        this.controller.updateLastExportedRecordPosition(record.getPosition());
    }

    public void close() {
        this.socket.close();
    }
}
