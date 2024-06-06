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
import { SimpleGroupxProjectDto } from './simpleGroupxProjectDto';


export interface UpdateDashboardDataRequest { 
    visibility: UpdateDashboardDataRequest.VisibilityEnum;
    groupxProject?: SimpleGroupxProjectDto;
}
export namespace UpdateDashboardDataRequest {
    export type VisibilityEnum = 'PUBLIC' | 'PRIVATE' | 'HIDDEN';
    export const VisibilityEnum = {
        Public: 'PUBLIC' as VisibilityEnum,
        Private: 'PRIVATE' as VisibilityEnum,
        Hidden: 'HIDDEN' as VisibilityEnum
    };
}


