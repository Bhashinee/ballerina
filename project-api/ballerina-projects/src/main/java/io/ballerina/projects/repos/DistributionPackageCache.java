/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package io.ballerina.projects.repos;

import io.ballerina.projects.Module;
import io.ballerina.projects.PackageCompilation;
import io.ballerina.projects.utils.ProjectConstants;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Distribution cache.
 *
 * @since 2.0.0
 */
public class DistributionPackageCache extends FileSystemRepository {

    public DistributionPackageCache() {
        super(Paths.get(System.getProperty(ProjectConstants.BALLERINA_INSTALL_DIR_PROP))
                .resolve(ProjectConstants.DIST_CACHE_DIRECTORY));
    }

    @Override
    public void cacheBir(Module module, byte[] bir) {
        // this is a read only repository
    }
}
