package com.dw.pract.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dw.pract.model.Attachment;
import com.dw.pract.model.Employee;

@RestController
public class CouchdbAPIController {

  @Inject
  private CouchDbConnector couchDbConnector;

  @RequestMapping(value = "/data/employee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public void create(@RequestBody Employee employee) {
    employee.setId(UUID.randomUUID().toString());
    couchDbConnector.create(employee);
  }

  @RequestMapping(value = "/data/employee/{id}/attachment", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void addAttachment(@RequestParam(value = "file") MultipartFile[] multipartFiles,
      @PathVariable(value = "id") String id) throws IllegalStateException, IOException {

    Employee emp = couchDbConnector.get(Employee.class, id);

    if (multipartFiles != null) {
      uploadAttachment(getAttachment(multipartFiles), emp.getId(), emp.getRevision());
    }
  }

  private void uploadAttachment(List<Attachment> attachments, String id, String revision) throws FileNotFoundException {

    if (CollectionUtils.isNotEmpty(attachments)) {
      for (Attachment attachment : attachments) {

        InputStream inputStream = new FileInputStream(attachment.getFile());
        AttachmentInputStream attachmentInputStream =
            new AttachmentInputStream(attachment.getFileName(), inputStream, attachment.getContentType());

        couchDbConnector.createAttachment(id, revision, attachmentInputStream);
      }
    }
  }

  private List<Attachment> getAttachment(MultipartFile[] multipartFiles) throws IllegalStateException, IOException {
    List<Attachment> attachments = new ArrayList<Attachment>();

    for (MultipartFile multipartFile : multipartFiles) {
      File file = getFile(multipartFile);

      Attachment attachment = new Attachment();
      attachment.setFileName(file.getName());
      attachment.setFile(file);
      attachment.setContentType(multipartFile.getContentType());

      attachments.add(attachment);
    }

    return attachments;
  }

  private File getFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
    // String type[] = StringUtils.split(multipartFile.getOriginalFilename(), ".");
    // String fileName = UUID.randomUUID().toString() + "." + type[1];
    File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
    multipartFile.transferTo(file);
    return file;
  }
}
