export type ConnectionOptions = {
    url: string | undefined;

    protocols: string | undefined;

    transformStruct: TransformDataStruct
};

export enum TransformDataStruct {
    JSON,
    PRUE_STRING,
    ARRAY_BUFFER,
    BLOB,
    ARRAY_BUFFER_VIEW,
}
