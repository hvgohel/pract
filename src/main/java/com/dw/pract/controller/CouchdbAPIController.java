package com.dw.pract.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.dw.pract.model.Attachment;
import com.dw.pract.model.EmpAddress;
import com.dw.pract.model.Employee;
import com.dw.pract.repository.EmpAddressRepository;
import com.dw.pract.repository.EmployeeRepository;
import com.dw.pract.utils.AppConfig;

@RestController
public class CouchdbAPIController {

  private static Logger logger = LoggerFactory.getLogger(CouchdbAPIController.class);

  @Inject
  private CouchDbConnector couchDbConnector;

  @Inject
  private EmployeeRepository employeeRepository;

  @Inject
  private EmpAddressRepository empAddressRepository;

  @Autowired
  private AppConfig appConfig;

  @RequestMapping(value = "/data/employee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public Employee create(@RequestBody Employee employee) {
    employee.setId(UUID.randomUUID().toString());
    couchDbConnector.create(employee);

    Set<EmpAddress> addresses = employee.getAddresses();
    if (CollectionUtils.isNotEmpty(addresses)) {
      for (EmpAddress address : addresses) {
        address.setEmpId(employee.getId());
        couchDbConnector.update(address);
      }
    }

    return couchDbConnector.get(Employee.class, employee.getId());
  }

  @RequestMapping(value = "/data/employee/{id}/attachment", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void addAttachment(@RequestParam(value = "file") MultipartFile[] multipartFiles,
      @PathVariable(value = "id") String id) throws IllegalStateException, IOException {

    Employee emp = couchDbConnector.get(Employee.class, id);

    if (multipartFiles != null) {
      String docId = emp.getId();
      String revisionNo = emp.getRevision();

      for (Attachment attachment : getAttachment(multipartFiles)) {
        revisionNo = uploadAttachment(docId, revisionNo, attachment);
      }
    }
  }

  private String uploadAttachment(String docId, String revisionNo, Attachment attachment) throws FileNotFoundException {

    InputStream inputStream = new FileInputStream(attachment.getFile());
    AttachmentInputStream attachmentInputStream =
        new AttachmentInputStream(attachment.getFileName(), inputStream, attachment.getContentType());

    return couchDbConnector.createAttachment(docId, revisionNo, attachmentInputStream);
  }

  private List<Attachment> getAttachment(MultipartFile[] multipartFiles) throws IllegalStateException, IOException {
    List<Attachment> attachments = new ArrayList<Attachment>();

    for (MultipartFile multipartFile : multipartFiles) {
      File file = getFile(multipartFile);
      String contentType = multipartFile.getContentType();
      attachments.add(getAttachment(file, contentType));
    }

    return attachments;
  }

  private Attachment getAttachment(File file, String contentType) {
    Attachment attachment = new Attachment();
    attachment.setFileName(file.getName());
    attachment.setFile(file);
    attachment.setContentType(contentType);
    return attachment;
  }

  private File getFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
    String type[] = StringUtils.split(multipartFile.getOriginalFilename(), ".");
    String fileName = UUID.randomUUID().toString() + "." + type[1];
    File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
    multipartFile.transferTo(file);
    return file;
  }

  @RequestMapping(value = "/data/employee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<Employee> getAll() {
    return employeeRepository.getAll();
  }

  @RequestMapping(value = "/data/address", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<EmpAddress> getAllAddress() {
    return empAddressRepository.getAll();
  }

  @RequestMapping(value = "/data/employee/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteEmployee(@PathVariable(value = "id") String id) {
    Employee employee = employeeRepository.get(id);
    String name = employee.getName();
    employeeRepository.remove(employee);
    logger.debug("employee {} deleted successfully", name);
    // couchDbConnector.delete(employee.getId(), employee.getRevision());

    // List<String> rev = new ArrayList<>();
    // List<Revision> revisions = couchDbConnector.getRevisions(id);
    // for (Revision r : revisions) {

    // rev.add(r.getRev());
    // }
    // Map<String, List<String>> map = new HashMap<>();
    // map.put(id, rev);
    // couchDbConnector.purge(map);

  }

  @RequestMapping(value = "/data/employee/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Employee getAllAddress(@PathVariable(value = "id") String id) {
    return employeeRepository.get(id);
  }

  @RequestMapping(value = "/data/emp-address", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<EmpAddress> getEmpAddress(@RequestParam(value = "city") String city) {
    return empAddressRepository.findByCity(city);
  }

  @RequestMapping(value = "/data/emp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<Employee> getEmployeeByName(@RequestParam(value = "name") String name) {
    return employeeRepository.findByName(name);
  }

  @RequestMapping(value = "/data/emp-address/count", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public int countAddress() {
    return empAddressRepository.count();
  }

  @RequestMapping(value = "/data/emp/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public int countEmployee() {
    return employeeRepository.count();
  }

  @RequestMapping(value = "/data/emp/byname", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<Employee> getEmpByName(@RequestParam(value = "name") String name) {
    return employeeRepository.byName(name);
  }

  @RequestMapping(value = "/data/download", method = RequestMethod.GET)
  @ResponseBody
  public File download(@RequestParam(value = "docId") String docId,
      @RequestParam(value = "attachmentId") String attachmentId) throws IOException {

    String url =
        appConfig.getCouchdbURL() + "/" + appConfig.getCouchdbDatabaseName() + "/" + docId + "/" + attachmentId;

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

    HttpEntity<String> entity = new HttpEntity<String>(headers);

    ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

    File file = new File(System.getProperty("java.io.tmpdir") + "/" + attachmentId);

    if (response.getStatusCode() == HttpStatus.OK) {
      FileUtils.writeByteArrayToFile(file, response.getBody());
    }

    String contentType = Files.probeContentType(file.toPath());
    Attachment attachment = getAttachment(file, contentType);

    Employee emp = employeeRepository.get("07416d715eed9e282b1b8fa26600084b");
    uploadAttachment(emp.getId(), emp.getRevision(), attachment);

    return file;
  }
}
