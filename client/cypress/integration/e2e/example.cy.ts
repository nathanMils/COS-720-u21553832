// https://on.cypress.io/api

describe('My First Test', () => {
  it('visits the app root url', () => {
    cy.visit('http://localhost:3000/')
    cy.contains('div', 'You did it!')
  })
})
