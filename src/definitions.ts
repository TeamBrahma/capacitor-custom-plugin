export interface CustomPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
