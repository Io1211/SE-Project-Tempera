/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { GroupxProject } from './groupxProject';
import { Userx } from './userx';


export interface Project { 
    id?: number;
    name?: string;
    description?: string;
    manager?: Userx;
    groupxProjects?: Set<GroupxProject>;
    active?: boolean;
}

