#!/usr/bin/env python3

import os
import re
import sys

def insert_version_after_artifactid(file_path, artifact_id, version):
    """
    Searches for <artifactId>artifact_id</artifactId> in the file and inserts
    <version>version</version> after it if not already present
    """
    with open(file_path, 'r') as file:
        content = file.read()
    
    # Construct the regex pattern to find the artifactId
    pattern = r'(<artifactId>' + re.escape(artifact_id) + r'</artifactId>)(\s*)(</dependency>|</plugin>)'
    
    # Check if version is already present
    version_pattern = r'<artifactId>' + re.escape(artifact_id) + r'</artifactId>\s*<version>[^<]+</version>'
    if re.search(version_pattern, content):
        print(f"Version already exists for {artifact_id} in {file_path}")
        return content, False
    
    # Replace with version inserted
    indentation = re.search(pattern, content)
    if indentation:
        indentation = indentation.group(2)
        replacement = r'\1\2<version>' + version + r'</version>\2\3'
        modified_content = re.sub(pattern, replacement, content)
        
        if modified_content != content:
            with open(file_path, 'w') as file:
                file.write(modified_content)
            print(f"Added version {version} for {artifact_id} in {file_path}")
            return modified_content, True
    
    print(f"Could not find {artifact_id} without version in {file_path}")
    return content, False

def main():
    print("Fixing POM versions with direct Python manipulation...")
    
    # Define the services and their POM paths
    services = [
        {
            'name': 'finance-service',
            'path': './services/finance/finance-service/pom.xml',
        },
        {
            'name': 'employee-service',
            'path': './services/hr/employee-service/pom.xml',
        },
        {
            'name': 'company-service',
            'path': './services/organization/company-service/pom.xml',
        },
    ]
    
    # Define the artifacts that need version
    artifacts = [
        {
            'id': 'springdoc-openapi-starter-webmvc-ui',
            'version': '2.3.0',
            'type': 'dependency'
        },
        {
            'id': 'jacoco-maven-plugin',
            'version': '0.8.10',
            'type': 'plugin'
        },
        {
            'id': 'dependency-check-maven',
            'version': '8.2.1',
            'type': 'plugin'
        },
    ]
    
    # Process each service's POM file
    for service in services:
        if os.path.exists(service['path']):
            print(f"Processing {service['name']} POM...")
            
            for artifact in artifacts:
                insert_version_after_artifactid(
                    service['path'], 
                    artifact['id'], 
                    artifact['version']
                )
        else:
            print(f"File not found: {service['path']}")
    
    print("POM version fixing completed!")

if __name__ == "__main__":
    main()