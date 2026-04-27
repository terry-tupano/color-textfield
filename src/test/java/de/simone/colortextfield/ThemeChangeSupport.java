/*-
 * #%L
 * TrendChart Add-on for vaadin Flow
 * %%
 * Copyright (C) 2026 Terry Tupano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.simone.colortextfield;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * A helper to allow changing the theme in the test views with a URL parameter.
 * E.g. ?theme=aura changes sessions default theme to aura.
 */
public class ThemeChangeSupport
        implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener( uiEvt -> {
            VaadinRequest request = VaadinRequest.getCurrent();

            // Note, as there is the "react router" and it "two-request init", this does not work!
            /*
            String parameter = request.getParameter("theme");
             */

            // But Vaadin does some hack and sends the original query string as "query" query parameter...
            String realQueryParameters = request.getParameter("query");

            String parameter = "";
            String[] split = realQueryParameters.split("=");
            for(int i = 0; i < split.length; i++) {
                if(split[i].equals("theme")) {
                    parameter = split[i+1];
                    break;
                }
            }

            VaadinSession session = uiEvt.getUI().getSession();
            if("lumo".equals(parameter)) {
                session.setAttribute("theme", Lumo.class);
            } else if ("aura".equals(parameter)) {
                // session.setAttribute("theme", Aura.class);
            } else if(!parameter.isEmpty()) {
                // anything else (themeless)
                session.setAttribute("theme", "void");
            }

            Object theme = session.getAttribute("theme");
            if(theme == null) {
                theme = Lumo.class;
            }
        });
    }

}