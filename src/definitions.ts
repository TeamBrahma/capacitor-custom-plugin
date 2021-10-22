export interface CustomPlugin {
    echo(options: { value: string }): Promise<{ value: string }>;
    getContacts(filter: string): Promise<{results: any[]}>;
}
