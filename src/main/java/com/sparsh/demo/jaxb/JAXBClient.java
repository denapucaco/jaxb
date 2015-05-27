package com.sparsh.demo.jaxb;

import java.io.File;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sparsh.medical.getpatientdetailsresponse.GetPatientDetailsResponse;
import com.sparsh.medical.getpatientdetailsresponse.Patient;
import com.sparsh.medical.getpatientdetailsresponse.PatientHistoryDetail;
import com.sparsh.medical.getpatientdetailsresponse.PatientHistoryDetails;
import com.sparsh.medical.getpatientdetailsresponse.Patients;

public class JAXBClient {

    public static void main(String[] args) {

        try {
            createObjectToXML();
        } catch (DatatypeConfigurationException | ParseException e) {
            e.printStackTrace();
        }

        createXMLToObject();
    }

    private static void createXMLToObject() {
        try {

            File file = new File("D:\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(GetPatientDetailsResponse.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            GetPatientDetailsResponse response = (GetPatientDetailsResponse) jaxbUnmarshaller.unmarshal(file);

            // output pretty printed

            Patients patients = response.getPatients();
            List<Patient> patientList = patients.getPatient();
            for (Patient patient : patientList) {
                System.out.println("Patient ID : " + patient.getPatientID());
                System.out.println("Patient Name : " + patient.getPatientName());
                System.out.println("Patient Age : " + patient.getPatientAge());
                System.out.println("Patient Status : " + patient.getPatientStatus());
                System.out.println("Doctor Name : " + patient.getDoctorName());
                System.out.println("Doctor Speciality : " + patient.getDoctorSpeciality());
                System.out.println("Admission Date : " + patient.getAdmissionDate());
                System.out.println("Discharge Date : " + patient.getDischargeDate());

                PatientHistoryDetails patientHistoryDetails = patient.getPatientHistoryDetails();
                List<PatientHistoryDetail> patientHistoryDetailList = patientHistoryDetails.getPatientHistoryDetail();

                for (PatientHistoryDetail patientHistoryDetail : patientHistoryDetailList) {
                    System.out.println("Decease : " + patientHistoryDetail.getDecease());
                    System.out.println("Description : " + patientHistoryDetail.getDescription());
                }

            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Object To XML
     * 
     * @throws DatatypeConfigurationException
     * @throws ParseException
     */
    private static void createObjectToXML() throws DatatypeConfigurationException, ParseException {
        GetPatientDetailsResponse response = new GetPatientDetailsResponse();

        Patient patient = new Patient();
        patient.setDoctorName("Prashant");
        patient.setDoctorSpeciality("General Physican");
        patient.setPatientAge(new BigInteger("31"));
        patient.setPatientID("1234");
        patient.setPatientName("Test");
        patient.setPatientStatus("Discharged");

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar dischargeDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        Date date = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = df.parse("2014-02-10 11:15:00");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar admissionDate =
                DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH), date.getHours(), date.getMinutes(), date.getSeconds(),
                        DatatypeConstants.FIELD_UNDEFINED, cal.getTimeZone().LONG).normalize();

        patient.setAdmissionDate(admissionDate);
        patient.setDischargeDate(dischargeDate);

        PatientHistoryDetail patientHistoryDetail = new PatientHistoryDetail();
        patientHistoryDetail.setDecease("Pain in Abdomain");

        patientHistoryDetail.setDescription("Food Poison");
        patientHistoryDetail.setStillOnMedication(false);

        List<PatientHistoryDetail> patientHistoryDetailList = new ArrayList<PatientHistoryDetail>();
        patientHistoryDetailList.add(patientHistoryDetail);

        PatientHistoryDetails patientHistoryDetails = new PatientHistoryDetails(patientHistoryDetailList);

        patient.setPatientHistoryDetails(patientHistoryDetails);

        List<Patient> patientList = new ArrayList<Patient>();
        patientList.add(patient);

        Patients patients = new Patients(patientList);

        response.setPatients(patients);

        try {

            File file = new File("D:\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(GetPatientDetailsResponse.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(response, file);
            jaxbMarshaller.marshal(response, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
