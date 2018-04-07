package org.java.rest.controller;

import org.java.rest.controller.dto.Transfer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("account")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface AccountController {

    @POST
    @Path("transfer")
    Response transfer(@NotNull @Valid Transfer transfer);
}
