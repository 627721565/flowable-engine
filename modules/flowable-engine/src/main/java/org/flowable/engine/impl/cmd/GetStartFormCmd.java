/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */

package org.flowable.engine.impl.cmd;

import java.io.Serializable;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.impl.form.StartFormHandler;
import org.flowable.engine.impl.interceptor.Command;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.Flowable5Util;
import org.flowable.engine.impl.util.FormHandlerUtil;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * @author Tom Baeyens
 */
public class GetStartFormCmd implements Command<StartFormData>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String processDefinitionId;

  public GetStartFormCmd(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }

  public StartFormData execute(CommandContext commandContext) {
    ProcessDefinition processDefinition = commandContext.getProcessEngineConfiguration().getDeploymentManager().findDeployedProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new FlowableObjectNotFoundException("No process definition found for id '" + processDefinitionId + "'", ProcessDefinition.class);
    }
    
    if (Flowable5Util.isFlowable5ProcessDefinition(processDefinition, commandContext)) {
      return Flowable5Util.getFlowable5CompatibilityHandler().getStartFormData(processDefinitionId);
    }

    StartFormHandler startFormHandler = FormHandlerUtil.getStartFormHandler(commandContext, processDefinition);
    if (startFormHandler == null) {
      throw new FlowableException("No startFormHandler defined in process '" + processDefinitionId + "'");
    }

    return startFormHandler.createStartFormData(processDefinition);
  }
  
}
