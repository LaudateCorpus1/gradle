/*
 * Copyright 2013 the original author or authors.
 *
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
 */

package org.gradle.nativebinaries.toolchain.internal.gcc;

import org.gradle.api.Action;
import org.gradle.api.tasks.WorkResult;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.nativebinaries.language.cpp.internal.CppCompileSpec;
import org.gradle.nativebinaries.toolchain.internal.ArgsTransformer;
import org.gradle.nativebinaries.toolchain.internal.CommandLineTool;
import org.gradle.nativebinaries.toolchain.internal.MacroArgsConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CppHeaderPrecompiler extends CppCompiler {
    private final CommandLineTool<CppCompileSpec> commandLineTool;

    public CppHeaderPrecompiler(CommandLineTool<CppCompileSpec> commandLineTool, Action<List<String>> argsAction, boolean useCommandFile) {
        ArgsTransformer<CppCompileSpec> argsTransformer = new CppHeaderPrecompileArgsTransformer();
        argsTransformer = new UserArgsTransformer<CppCompileSpec>(argsTransformer, argsAction);
        if (useCommandFile) {
            argsTransformer = new GccOptionsFileArgTransformer<CppCompileSpec>(argsTransformer);
        }
        this.commandLineTool = commandLineTool.withArguments(argsTransformer);
    }

    public WorkResult execute(CppCompileSpec spec) {
        return commandLineTool.inWorkDirectory(spec.getObjectFileDir()).execute(spec);
    }

    private static class CppHeaderPrecompileArgsTransformer extends GccCompilerArgsTransformer<CppCompileSpec> {
        @Override
        public List<String> transform(CppCompileSpec spec) {
            List<String> args = new ArrayList<String>();
            Collections.addAll(args, "-x", getLanguage());

            for (String macroArg : new MacroArgsConverter().transform(spec.getMacros())) {
                args.add("-D" + macroArg);
            }

            args.addAll(spec.getAllArgs());
            args.add("-c");
            if (spec.isPositionIndependentCode()) {
                if (!OperatingSystem.current().isWindows()) {
                    args.add("-fPIC");
                }
            }

            for (File file : spec.getIncludeRoots()) {
                args.add("-I");
                args.add(file.getAbsolutePath());
            }
            for (File file : spec.getPrecompiledHeaders()) {
                args.add(file.getAbsolutePath());
            }
            return args;
        }

        protected String getLanguage() {
            return "c++-header";
        }
    }
}
