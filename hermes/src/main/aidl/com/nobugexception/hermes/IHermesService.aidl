package com.nobugexception.hermes;

import com.nobugexception.hermes.Request;
import com.nobugexception.hermes.Responce;
import com.nobugexception.hermes.EventMessage;

interface IHermesService {

    Responce send(in Request request);

    void post(in EventMessage event);

}
