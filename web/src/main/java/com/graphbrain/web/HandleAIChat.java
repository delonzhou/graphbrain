package com.graphbrain.web;

import com.graphbrain.db.ProgNode;
import com.graphbrain.db.UserNode;
import com.graphbrain.eco.Context;
import com.graphbrain.eco.Contexts;
import com.graphbrain.eco.Prog;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class HandleAIChat extends Route {

    private Prog prog;

    public HandleAIChat(String route) {
        super(route);

        ProgNode prognode = WebServer.graph.getProgNode("prog/prog");
        if (prognode != null) {
            prog = Prog.fromString(prognode.getProg());
        }
        else {
            prog = null;
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        //UserNode userNode = WebServer.getUser(request);
        String sentence = request.queryParams("sentence");
        //String rootId = request.queryParams("rootId");

        WebServer.log(request, "user said: " + sentence);

        String edgeStr = "";

        List<Contexts> ctxtsList = prog.wv(sentence, 0);

        for (Contexts ctxts : ctxtsList) {
            for (Context ctxt : ctxts.getCtxts()) {
                edgeStr = ctxt.getTopRetVertex().toString();
            }
        }

        JSONObject json = new JSONObject();
        json.put("sentence", edgeStr);
        json.put("newedges", "");
        json.put("goto", "");

        return json.toString();
    }
}