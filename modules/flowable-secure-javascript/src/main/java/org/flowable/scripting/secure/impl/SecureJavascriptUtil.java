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
package org.flowable.scripting.secure.impl;

import org.flowable.engine.delegate.VariableScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * @author Joram Barrez
 */
public class SecureJavascriptUtil {

  public static Object evaluateScript(VariableScope variableScope, String script) {
    Context context = Context.enter();
    try {
        Scriptable scope = context.initStandardObjects();
        SecureScriptScope secureScriptScope = new SecureScriptScope(variableScope);
        scope.setPrototype(secureScriptScope);

        return context.evaluateString(scope, script, "<script>", 0, null);
    } finally {
        Context.exit();
    }
  }
  
}
