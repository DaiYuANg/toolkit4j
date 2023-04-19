export type ConnectionOptions = {
    url: string;

    protocols: string[] | undefined;

    transformStruct: TransformDataStruct;

    global: boolean
};

export enum TransformDataStruct {
    JSON,
    PRUE_STRING,
    ARRAY_BUFFER,
    BLOB,
    ARRAY_BUFFER_VIEW,
}
