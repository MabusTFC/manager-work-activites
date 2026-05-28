package com.example.managerworkactivites.service;

import com.example.managerworkactivites.dto.ActionRequest;
import com.example.managerworkactivites.dto.ActionResponse;
import java.util.List;


public interface ActionService {

    //get action
    List<ActionResponse> getAllActions();

    ActionResponse getActionById(Long id);
    //post action
    ActionResponse createAction(ActionRequest request);

    ActionResponse updateAction(Long id, ActionRequest request);
    void deleteAction(Long id);
}
